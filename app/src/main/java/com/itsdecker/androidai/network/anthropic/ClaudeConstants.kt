package com.itsdecker.androidai.network.anthropic

// Constant Values Related to Anthropic API

// Versions
const val ANTHROPIC_VERSION = "2023-06-01"

// API Urls
const val ANTHROPIC_BASE_URL = "https://api.anthropic.com/v1/"

// Max Tokens
const val ANTHROPIC_API_DEFAULT_MAX_TOKENS = 1024

// Models
const val ANTHROPIC_CLAUDE_3_5_MODEL_SONNET = "claude-3-5-sonnet-latest"
const val ANTHROPIC_CLAUDE_3_5_MODEL_HAIKU = "claude-3-5-haiku-latest"
const val ANTHROPIC_CLAUDE_3_MODEL_OPUS = "claude-3-opus-latest"
const val ANTHROPIC_CLAUDE_3_MODEL_SONNET = "claude-3-sonnet-20240229"
const val ANTHROPIC_CLAUDE_3_MODEL_HAIKU = "claude-3-sonnet-20240229"
const val ANTHROPIC_CLAUDE_MODEL_DEFAULT = ANTHROPIC_CLAUDE_3_MODEL_HAIKU

// Roles - TODO - Convert to an enum with retrofit annotations
const val ANTHROPIC_MESSENGER_ROLE_USER = "user"
const val ANTHROPIC_MESSENGER_ROLE_ASSISTANT = "assistant"

// Content Types - TODO - Fill in more types and convert to enum with retrofit annotations
const val ANTHROPIC_API_TEXT_CONTENT_TYPE = "text"
const val ANTHROPIC_API_DEFAULT_CONTENT_TYPE = ANTHROPIC_API_TEXT_CONTENT_TYPE