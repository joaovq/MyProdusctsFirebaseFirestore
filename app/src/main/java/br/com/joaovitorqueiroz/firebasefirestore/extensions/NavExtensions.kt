package br.com.joaovitorqueiroz.firebasefirestore.extensions

import android.support.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import br.com.joaovitorqueiroz.firebasefirestore.R

private val defaultOptions = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_in_up)
    .setExitAnim(R.anim.slide_out_up)
    .setPopEnterAnim(R.anim.slide_in_down)
    .setPopExitAnim(R.anim.slide_out_down).build()


fun NavController.navigateWithAnim(
    navDirections : NavDirections,
    navOptions : NavOptions = defaultOptions
){
    this.navigate(navDirections, navOptions)
}

fun NavController.navigateWithAnim(
    @IdRes resId : Int,
    navOptions : NavOptions = defaultOptions
){
    this.navigate(resId,null, navOptions)
}