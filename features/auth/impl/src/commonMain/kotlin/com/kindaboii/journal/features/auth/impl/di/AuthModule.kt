package com.kindaboii.journal.features.auth.impl.di

import com.kindaboii.journal.domain.AuthService
import com.kindaboii.journal.AuthFeatureApi
import com.kindaboii.journal.features.auth.impl.AuthFeatureApiImpl
import com.kindaboii.journal.features.auth.impl.data.SupabaseAuthService
import com.kindaboii.journal.features.auth.impl.presentation.AuthViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val authModule = module {
    singleOf(::SupabaseAuthService) { bind<AuthService>() }
    singleOf(::AuthFeatureApiImpl) { bind<AuthFeatureApi>() }

    viewModelOf(::AuthViewModel)
}
