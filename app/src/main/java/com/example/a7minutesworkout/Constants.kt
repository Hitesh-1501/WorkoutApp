package com.example.a7minutesworkout

import kotlin.math.E


object Constants {

    fun defaultExerciseList(): ArrayList<ExerciseModel> {

        val exerciseList = ArrayList<ExerciseModel>()

        val jumpingjacks =  ExerciseModel(
            1,
            "Jumping Jacks",
            false,
            false,
            R.raw.jumpingjacks
        )

        exerciseList.add(jumpingjacks)

        val handsup = ExerciseModel(
            2,
            "Hands Ups",
            false,
            false,
            R.raw.handups
        )

        exerciseList.add(handsup)

        val leg = ExerciseModel(
            3,
            "Leg Exercise",
            false,
            false,
            R.raw.leg
        )

        exerciseList.add(leg)

        val abdominalcrunches = ExerciseModel(
            4,
            "Abdominal Crunches",
            false,
            false,
            R.raw.abdominalcrunches
        )

        exerciseList.add(abdominalcrunches)

        val plank = ExerciseModel(
            5,
            "Plank",
            false,
            false,
            R.raw.plank
        )

        exerciseList.add(plank)

        val  shoulderstretch = ExerciseModel(
            6,
            "Shoulder Stretch",
            false,
            false,
            R.raw.shoulderstretch
        )

        exerciseList.add(shoulderstretch)

        val pushups = ExerciseModel(
            7,
            "Push Ups",
            false,
            false,
            R.raw.pushups
        )

        exerciseList.add(pushups)

        val standup = ExerciseModel(
            8,
            "Stand Up",
            false,
            false,
            R.raw.standup
        )

        exerciseList.add(standup)

        val triceps = ExerciseModel(
            9,
            "Triceps Dips",
            false,
            false,
            R.raw.triceps
        )
        exerciseList.add(triceps)

        return exerciseList

    }

}