package com.sergeenko.alexey.noble.interfaces

import com.hbb20.CountryCodePicker

interface ClientInfoInput {
    fun setPhone(ccp: CountryCodePicker?)
    fun getDefaultPhoneCode(): String?
    fun updateCountryCode(selectedCountryNameCode: String?)
    fun setAge(current: String, toString: String)
    fun setName(trim: String)
    fun setSurname(trim: String)
    fun setPatronymic(trim: String)
    fun setWeight(trim: String)
    fun setHeight(trim: String)
    fun isPhoneNotNull(): Boolean
    fun isAgeNotNull(): Boolean
    fun isClientNameNotNull(): Boolean
    fun isSurnameNotNull(): Boolean
    fun setSex(s: String)

}
