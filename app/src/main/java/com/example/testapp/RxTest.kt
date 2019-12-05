package com.example.testapp

import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.TimeUnit

class RxTest : ObservableOnSubscribe<Int>{

    private val list = listOf(1, 2, 3, 4, 5)

    override fun subscribe(emitter: ObservableEmitter<Int>) {
        for(l in list) {
            emitter.onNext(l)
            TimeUnit.SECONDS.sleep(1)
        }
        emitter.onComplete()
    }


}