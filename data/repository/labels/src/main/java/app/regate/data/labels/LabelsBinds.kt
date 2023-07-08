package app.regate.data.labels

import me.tatarka.inject.annotations.Provides

interface LabelsBinds {
    @Provides
    fun provideLabelDataSource(
        bind: LabelDataSourceImpl
    ): LabelsDataSource = bind
}