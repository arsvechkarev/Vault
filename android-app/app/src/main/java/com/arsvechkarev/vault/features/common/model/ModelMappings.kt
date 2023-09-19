package com.arsvechkarev.vault.features.common.model

import buisnesslogic.model.CreditCard
import buisnesslogic.model.PasswordEntry
import buisnesslogic.model.PlainText

fun PasswordEntry.toPasswordItem(): PasswordItem {
  return PasswordItem(
    id = id,
    websiteName = websiteName,
    login = login,
    password = password,
    notes = notes
  )
}

fun CreditCard.toCreditCardItem(): CreditCardItem {
  return CreditCardItem(
    id = id,
    cardNumber = cardNumber,
    expirationDate = expirationDate,
    cardholderName = cardholderName,
    cvcCode = cvcCode,
    pinCode = pinCode,
    notes = notes
  )
}

fun PlainText.toPlainTextItem(): PlainTextItem {
  return PlainTextItem(
    id = id,
    title = title,
    text = text
  )
}

fun PasswordItem.toPassword(): PasswordEntry {
  return PasswordEntry(
    id = id,
    websiteName = websiteName,
    login = login,
    password = password,
    notes = notes
  )
}

fun CreditCardItem.toCreditCard(): CreditCard {
  return CreditCard(
    id = id,
    cardNumber = cardNumber,
    expirationDate = expirationDate,
    cardholderName = cardholderName,
    cvcCode = cvcCode,
    pinCode = pinCode,
    notes = notes
  )
}

fun PlainTextItem.toPlainText(): PlainText {
  return PlainText(
    id = id,
    title = title,
    text = text
  )
}
