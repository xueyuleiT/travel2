package com.glcxw.router

class RouterUtil {

    companion object{
        val routers = mutableMapOf<String, IRouter>()

        fun <T : IRouter> add(cls : Class<T>){
            val router = cls.newInstance() as IRouter
            routers[cls.name] = router
        }

        fun add(router : IRouter){
            routers[router.javaClass.name] = router
        }

        fun <T : IRouter> remove(cls : Class<T>){
            routers.remove(cls.name)
        }

        fun clear(){
            routers.clear()
        }

        fun  get(key : String): IRouter?{
            return routers[key]
        }
    }

}