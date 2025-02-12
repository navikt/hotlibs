package org.junit.rules

import org.junit.runner.Description
import org.junit.runners.model.Statement

interface TestRule {
    fun apply(base: Statement, description: Description): Statement
}
