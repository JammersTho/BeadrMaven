package model

data class Payment (val paymentId : Int,
                    val method : String,
                    val amount: Double)