package com.intelliteq.fea.ammocalculator.formulas

class BasicPerWeaponFormula(
    val totalAmmoNeeded : Int,
    val numberWeapons : Int
) : Formula {

    override fun calculate(): Int {
        return totalAmmoNeeded / numberWeapons
    }
}