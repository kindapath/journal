package com.kindaboii.journal.features.profile.impl.di

import com.kindaboii.journal.features.profile.api.ProfileFeatureApi
import com.kindaboii.journal.features.profile.impl.ProfileFeatureApiImpl
import com.kindaboii.journal.features.profile.impl.presentation.ProfileViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val profileModule = module {
    singleOf(::ProfileFeatureApiImpl) { bind<ProfileFeatureApi>() }
    viewModelOf(::ProfileViewModel)
}
