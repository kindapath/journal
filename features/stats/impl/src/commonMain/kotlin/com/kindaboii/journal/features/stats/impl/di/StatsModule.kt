package com.kindaboii.journal.features.stats.impl.di

import com.kindaboii.journal.features.stats.api.StatsFeatureApi
import com.kindaboii.journal.features.stats.impl.StatsFeatureApiImpl
import com.kindaboii.journal.features.stats.impl.presentation.StatsViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val statsModule = module {
    singleOf(::StatsFeatureApiImpl) { bind<StatsFeatureApi>() }
    viewModelOf(::StatsViewModel)
}
