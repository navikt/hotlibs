#!/usr/bin/env bun

/// <reference lib="dom" />

// @ts-ignore
import { $ } from "bun"

await $`bun --version`

interface Versioned {
  version: string | { ref: string }
}

interface Library extends Versioned {
  group: string
  name: string
}

interface Plugin extends Versioned {
  id: string
}

interface VersionCatalog {
  versions: Record<string, string>
  plugins: Record<string, Plugin>
  libraries: Record<string, Library>
  bundles: Record<string, string[]>
}

interface SearchResponse {
  responseHeader: Record<string, any>
  response: {
    numFound: number
    start: number
    docs: Array<{
      id: string
      g: string
      a: string
      latestVersion: string
      timestamp: number
      versionCount: number
    }>
  }
  spellcheck: Record<string, any>
}

async function search(libraries: Library[]): Promise<SearchResponse> {
  const query = libraries
    .map(({ group, name }) => `(g:${group}+AND+a:${name})`)
    .join("+OR+")
  const url = `https://search.maven.org/solrsearch/select?q=${query}`
  console.log(url)
  const response = await fetch(url)
  if (response.ok) {
    return await response.json()
  }
  throw new Error(response.statusText || "Fail!")
}

async function readVersionCatalog(file: string): Promise<{
  versionCatalog: VersionCatalog
  getVersion({ version }: Versioned): string
}> {
  const versionCatalog: VersionCatalog = await import(file)
  return {
    versionCatalog,
    getVersion({ version }: Versioned): string {
      return typeof version === "string"
        ? version
        : versionCatalog.versions[version.ref]
    },
  }
}

const {
  versionCatalog: { plugins, libraries },
  getVersion,
} = await readVersionCatalog("../gradle/libs.versions.toml")

const result = await search(Object.values(libraries).slice(0, 1))

Object.entries(libraries).forEach(([name, library]) => {
  const currentVersion = getVersion(library)
  const newVersion = "TODO"
  const id = `${library.group}:${library.name}`
  console.log(`${id}:${currentVersion} -> ${id}:${newVersion}`)
})
