package ca.burchill.cointracker.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ca.burchill.cointracker.database.CoinsDatabase
import ca.burchill.cointracker.database.asDomainModel
import ca.burchill.cointracker.domain.Coin
import ca.burchill.cointracker.network.CoinApi
import ca.burchill.cointracker.network.databaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class CoinsRepository (private val database: CoinsDatabase) {

    // TODO expose LiveData list of coins to observe
    val coins: LiveData<List<Coin>> = Transformations.map(database.coinDao.getCoins()) {
        it.asDomainModel()
    }

    //TODO
    suspend fun refreshCoins() {
        withContext(Dispatchers.IO) {

            val coinList = CoinApi.retrofitService.getCoins()
            database.coinDao.insertAll(databaseModel(coinList.coins))
        }
    }
}