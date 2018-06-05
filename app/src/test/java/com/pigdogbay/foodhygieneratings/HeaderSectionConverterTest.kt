package com.pigdogbay.foodhygieneratings

import com.pigdogbay.foodhygieneratings.model.*
import org.junit.Test
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

import org.junit.Assert.*


class HeaderSectionConverterTest {
    private val STOKE_RESOURCE = "stoke.json"

    @Throws(IOException::class)
    internal fun getEstablishments(resourePath: String): List<Establishment>? {
        val path = javaClass.classLoader.getResource(resourePath)!!.path
        val json = String(Files.readAllBytes(Paths.get(path)))
        return DummyDataProvider(json).findEstablishments(Query())
    }

    @Test
    fun convert1(){
        val establishments = getEstablishments(STOKE_RESOURCE)
        val converted = HeaderSectionConverter.convert(establishments!!, SearchType.local)
        assertEquals(109,converted.size)
        assertEquals("TAKEAWAY/SANDWICH SHOP (15)", converted[0])
        assertEquals("Riverside Fish Bar", (converted[1] as Establishment).business.name)
    }

    @Test
    fun convert2(){
        val establishments = getEstablishments(STOKE_RESOURCE)
        val converted = HeaderSectionConverter.convert(establishments!!, SearchType.map)
        assertEquals(109,converted.size)
        assertEquals("RESTAURANT/CAFE/CANTEEN (11)", converted[16])
        assertEquals("Furama Palace", (converted[18] as Establishment).business.name)
    }

    @Test
    fun convert3(){
        val establishments = getEstablishments(STOKE_RESOURCE)
        val converted = HeaderSectionConverter.convert(establishments!!, SearchType.quick)
        assertEquals(109,converted.size)
        assertEquals("TAKEAWAY/SANDWICH SHOP (15)", converted[0])
        assertEquals("Allo Pizza", (converted[1] as Establishment).business.name)
    }

}