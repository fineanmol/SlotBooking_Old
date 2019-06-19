package com.example.slotbookingv2.drawerItems

import com.example.slotbookingv2.R
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem

class CustomCenteredPrimaryDrawerItem : PrimaryDrawerItem() {

    override val layoutRes: Int
        get() = R.layout.material_drawer_item_primary_centered

    override val type: Int
        get() = R.id.material_drawer_item_centered_primary

}
