package com.gymtutor.gymtutor.commonusers.workout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/workout")
public class WorkoutController {

    @Autowired
    private WorkoutService workoutService;


}
