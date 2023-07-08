package app.regate.compoundmodels

data class InstalacionCategoryCount(
    val category_name:String,
    val category_id:Int,
    val count:Int,
    val thumbnail:String
)

data class InstalacionCategory(
    val category_name:String,
    val category_id:Int,
)