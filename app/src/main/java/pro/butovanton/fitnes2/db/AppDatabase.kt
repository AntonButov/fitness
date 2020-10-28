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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pro.butovanton.fitnes2.App.Companion.app


/**
 * The Room database for this app
 */
@Database(entities = [Data::class], version = 1, exportSchema = false)
//@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): FitnessDao

    companion object {
        var DB = Room.databaseBuilder(app, AppDatabase::class.java, "db_data")
            .allowMainThreadQueries()
            .build()
    }
}


