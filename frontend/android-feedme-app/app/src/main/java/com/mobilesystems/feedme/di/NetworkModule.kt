package com.mobilesystems.feedme.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

/**
 * Use basic auth for all except for Login.
 */

@Module
@InstallIn(ActivityComponent::class)
object NetworkModule {

}