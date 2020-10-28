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

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * The Data Access Object for the Plant class.
 */
@Dao
interface FitnessDao {
    @Query("SELECT * FROM DATA ORDER BY date")
    fun getData(): List<Data>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<Data>)

    @Query("DELETE FROM data")
    fun delete()
}
