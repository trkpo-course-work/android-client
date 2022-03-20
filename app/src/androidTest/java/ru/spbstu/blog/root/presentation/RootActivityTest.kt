package ru.spbstu.blog.root.presentation


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.spbstu.blog.R

@LargeTest
@RunWith(AndroidJUnit4::class)
class RootActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(RootActivity::class.java)

    @Test
    fun rootActivityTest() {
        val button = onView(
            allOf(
                withId(R.id.frg_login__mb_action_button), withText("ВОЙТИ"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))
    }
}
