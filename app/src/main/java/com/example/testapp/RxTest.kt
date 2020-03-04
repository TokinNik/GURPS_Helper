package com.example.testapp

import com.example.testapp.util.RollUtil
import io.reactivex.*
import io.reactivex.Observable
import toothpick.Toothpick
import toothpick.ktp.delegate.inject
import java.util.*
import java.util.concurrent.TimeUnit

class RxTest {

    private val rollUtil: RollUtil by inject()

    init {
        val appScope = Toothpick.openScope("APP")
        Toothpick.inject(this, appScope)
    }

    fun rxCreateRollWithTime(count: Int): Observable<Int> = Observable.create<Int> {
            for(i in 1..count) {
                it.onNext(rollUtil.roll3D6())
                TimeUnit.SECONDS.sleep(1)
            }
            it.onComplete()
        }

    fun rxTimerRoll(): Flowable<Int> = Observable.interval(1, TimeUnit.SECONDS)
        .map { rollUtil.roll3D6() }.toFlowable(BackpressureStrategy.MISSING)


    fun rxDeferCurrentTime() = Observable.defer {//defer ne nuzhen?
        Observable.just("${Date(System.currentTimeMillis()).hours}:${Date(System.currentTimeMillis()).minutes}")
    }
}