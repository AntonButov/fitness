/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pro.butovanton.fitnes2.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data")
data class Data(
    @PrimaryKey
    @ColumnInfo(name = "date")
    val created: Long,
    val device: String? = "",
    val heatRate: Int,
    val pressureDiastol: Int,
    val pressureSystol: Int,
    val oxygen: Int,
    val sugar: Int,
    val temperature: Float,
    val breathung: Int,
    val latitude: Double?,
    val longitude: Double?
)
