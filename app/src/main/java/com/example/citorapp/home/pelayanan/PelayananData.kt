package com.example.citorapp.home.pelayanan

object PelayananData {
    private val NamaCuciMotor = arrayOf(
        "Jaya Makmur Wash",
        "Putra Cuci Motor",
        "Klaker Wash"
    )

    private val AlamatCuciMotor = arrayOf(
        "Jl. MT Haryono IX No.13",
        "Jl.. MT Haryono II No. 9",
        "JL. Klayatan 3 No.16C"
    )

    private val status = arrayOf(
        "BUKA HARI INI",
        "BUKA HARI INI",
        "BUKA HARI INI"
    )

    val listCuci: ArrayList<PelayananModel>
    get(){
        val list = arrayListOf<PelayananModel>()
        for (position in NamaCuciMotor.indices){
            val cuci = PelayananModel()
            cuci.name = NamaCuciMotor[position]
            cuci.address = AlamatCuciMotor[position]
            cuci.status = status[position]

            list.add(cuci)
        }
        return list
    }
}