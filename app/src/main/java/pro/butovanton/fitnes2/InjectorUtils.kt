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

package pro.butovanton.fitnes2

import android.content.Context
import pro.butovanton.fitnes2.db.AppDatabase
import pro.butovanton.fitnes2.db.FitnessDao
import pro.butovanton.fitness.net.Api
import pro.butovanton.fitness.net.JSONPlaceHolderApi
import pro.butovanton.fitness.net.NetworkService

object InjectorUtils {

    fun provideApi() : Api {
        return Api(provideJSONPlaceHolderApi())
    }

    fun provideBatary() : Batary {
        return Batary()
    }

    private fun provideJSONPlaceHolderApi() : JSONPlaceHolderApi {
        return NetworkService.instance!!.jSONApi!!
    }

    fun provideDevice() : Device {
        return Device()
    }

    fun provideLocation() : LocationClass {
        return LocationClass(context = provideContext())
    }

    fun provideContext() : Context {
        return (App).app
    }

    fun provideAnaliser() : Analiser {
        return Analiser()
    }

    fun provideDao() : FitnessDao {
        return provideDatabase().getDao()
    }

    fun provideDatabase(): AppDatabase {
        return AppDatabase.DB
    }

}
