package model

data class Order (val orderId : Int,
                  val date: String,
                  val expectedDeliveryDate : String)