package com.arsvechkarev.vault.features.common.model

import android.os.Parcel
import android.os.Parcelable
import buisnesslogic.Password

class ParcelablePassword(val password: Password) : Parcelable {
  
  constructor(parcel: Parcel) : this(Password.fromRaw(parcel.createByteArray()!!))
  
  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeByteArray(password.encryptedData)
  }
  
  override fun describeContents(): Int {
    return 0
  }
  
  companion object CREATOR : Parcelable.Creator<ParcelablePassword> {
    override fun createFromParcel(parcel: Parcel): ParcelablePassword {
      return ParcelablePassword(parcel)
    }
    
    override fun newArray(size: Int): Array<ParcelablePassword?> {
      return arrayOfNulls(size)
    }
  }
}
