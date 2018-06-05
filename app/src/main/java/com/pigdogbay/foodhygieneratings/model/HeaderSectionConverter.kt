package com.pigdogbay.foodhygieneratings.model

import java.util.*

object HeaderSectionConverter {

    /*
        Groups a list of establishments by business type.
        String headers are created for each section
        Sections are sorted by name or distance depending on the search type
     */
    fun convert(establishments : List<Establishment>, searchType: SearchType) : List<Any> {
        val convertedList = ArrayList<Any>()
        establishments
            .groupBy { it.business.typeId}
                .toSortedMap(compareBy {Business.getBusinessSortOrder(it)})
                .forEach { key, value ->
                    convertedList.add(String.format(Locale.UK,"%s (%d)", Business.getBusinessType(key).toUpperCase(), value.size))
                    when (searchType){
                        SearchType.local -> value.sortedBy { it.distance }
                        SearchType.quick -> value.sortedBy { it.business.name }
                        SearchType.advanced -> value.sortedBy { it.business.name }
                        SearchType.map -> value.sortedBy { it.distance }
                    }.forEach { convertedList.add(it) }
                }
        return convertedList
    }
}