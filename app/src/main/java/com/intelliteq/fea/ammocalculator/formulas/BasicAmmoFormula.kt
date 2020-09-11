package com.intelliteq.fea.ammocalculator.formulas

class BasicAmmoFormula(
    val assaultType : Int = 1,
    val numberOfdays : Int = 1,
    val numberWeapons : Int = 1
) : Formula {

    override fun calculate(): Int {
        return assaultType * numberOfdays * numberWeapons
    }

}