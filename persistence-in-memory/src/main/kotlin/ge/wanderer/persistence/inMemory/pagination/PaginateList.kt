package ge.wanderer.persistence.inMemory.pagination

import ge.wanderer.common.listing.ListingParams

fun <T> List<T>.paginate(params: ListingParams): List<T> {

    if (this.isEmpty()) {
        return this
    }

    val maxIndex = params.batchSize * params.batchNumber
    val minIndex = maxIndex - params.batchSize
    val lastIndex = this.indexOf(this.last())

    if (minIndex > lastIndex) {
        return listOf()
    }
    if (maxIndex > lastIndex) {
        return this.slice(IntRange(minIndex, lastIndex))
    }
    return this.slice(IntRange(minIndex, maxIndex - 1))
}