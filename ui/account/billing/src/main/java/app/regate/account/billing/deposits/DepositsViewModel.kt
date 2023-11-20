package app.regate.account.billing.deposits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import app.regate.data.account.AccountRepository
import app.regate.data.dto.account.billing.DepositDto
import app.regate.domain.pagination.account.DeposistsPagination
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class DepositsViewModel(
    private val accountRepository: AccountRepository
) : ViewModel(){
    val pagedList: Flow<PagingData<DepositDto>> = Pager(PAGING_CONFIG){
        DeposistsPagination(fetch = {page->
            accountRepository.getDepositPagination(page)
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