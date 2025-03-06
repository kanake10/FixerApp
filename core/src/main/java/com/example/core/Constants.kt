/*
 * Copyright 2025 Ezra Kanake.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.core

object Constants {
    // hide api key
    const val BASE_URL = "https://data.fixer.io/api/"
    const val API_KEY = "5dfa9204620b5076a02960a52bfecb19"
    const val DB_NAME = "currency.db"
    const val GET_EXCHANGE_RATES = "$BASE_URL/latest"
    const val GET_CURRENCY_SYMBOLS = "$BASE_URL/symbols"
}
