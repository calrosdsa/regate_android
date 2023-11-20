package app.regate.account.billing.consume

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.data.account.AccountRepository
import app.regate.data.dto.account.billing.ConsumeDto
import app.regate.domain.pagination.account.ConsumesPagination
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ConsumeViewModel(
    private val accountRepository: AccountRepository
) : ViewModel(){
    val pagedList: Flow<PagingData<ConsumeDto>> = Pager(PAGING_CONFIG){
        ConsumesPagination(fetch = {page->
            accountRepository.getConsumePagination(page)
        })
    }.flow.cachedIn(viewModelScope)

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 20,
            initialLoadSize = 20,
            prefetchDistance = 5
        )
    }
}