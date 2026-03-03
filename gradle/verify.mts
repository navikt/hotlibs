#!/usr/bin/env bun

// @ts-ignore
import libs from "./libs.versions.toml"

interface Version {
  ref: string
}

const versions: Record<string, string> = libs.versions
const plugins: Record<string, { id: string; version: Version } | string> = libs.plugins
const libraries: Record<string, { module: string; version: Version } | string> = libs.libraries

Object.entries(plugins).map(([alias, plugin]) => {
  const id = typeof plugin === "string" ? plugin : plugin.id
  const parts = id.split(".")
  const lastPart = parts.at(-1)
  if (alias !== lastPart) {
    console.log(alias, id)
  }
})

console.log()

Object.entries(libraries).map(([alias, library]) => {
  const module = typeof library === "string" ? library : library.module
  const [groupId, artifactId, version] = module.split(":")
  if (alias !== artifactId.replace("-jvm", '')) {
    console.log(alias, groupId, artifactId, version)
  }
})
