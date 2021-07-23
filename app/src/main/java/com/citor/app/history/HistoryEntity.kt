package com.citor.app.history

data class HistoryEntity(
    var nama: String = "",
    var nama_mitra: String = "",
    var layanan: String = "",
    var kode_pemesanan: String = "",
    var metode_pembayaran: String = "",
    var harga: String = "",
    var timestamp: String = "",
    var status: String = ""
)