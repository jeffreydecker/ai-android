package com.itsdecker.androidai.network.openai

// API Urls
const val OPEN_AI_BASE_URL = "https://api.openai.com/v1/"

// Models
const val OPEN_AI_GPT_4o_MINI = "gpt-4o-mini"
const val OPEN_AI_GPT_4o = "chatgpt-4o-latest"
const val OPEN_AI_GPT_o1_MINI = "o1-mini"
const val OPEN_AI_GPT_o1 = "o1"
const val OPEN_AI_MODEL_DEFAULT = OPEN_AI_GPT_4o_MINI

// Tokens
const val OPEN_AI_DEFAULT_MAX_TOKENS = 1024

// Temperature
const val OPEN_AI_DEFAULT_TEMPERATURE = 1.0

const val OPEN_AI_ROLE_USER = "user"
const val OPEN_AI_ROLE_ASSISTANT = "assistant"
const val OPEN_AI_ROLE_SYSTEM = "developer"