package com.george.android.tasker.notes;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.george.android.tasker.MainActivity;
import com.george.android.tasker.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateNewNoteTest {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityTestRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void fillDataToNote() {
        onView(ViewMatchers.withId(R.id.navigation_note))
                .perform(click());

        onView(withId(R.id.buttonAddNote))
                .perform(click());

        onView(withId(R.id.editTextNoteTitle))
                .perform(typeText("title note"));
        onView(withId(R.id.editTextNoteDescription))
                .perform(typeText("note_description"));

        pressBack();

    }


}
