package ru.spbstu.blog.root.presentation


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.spbstu.blog.R
import ru.spbstu.blog.root.presentation.utils.waitFor
import kotlin.random.Random

@LargeTest
@RunWith(AndroidJUnit4::class)
class ResetPasswordTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(RootActivity::class.java, true)

    @Test
    fun resetPasswordTest() {

        onView(isRoot()).perform(waitFor(1000))

        val appCompatTextView = onView(
            allOf(
                withId(R.id.layout_login__tv_new_code), withText("Забыли пароль?"),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_login__rl_root),
                        childAtPosition(
                            withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                            0
                        )
                    ),
                    2
                )
            )
        )
        appCompatTextView.perform(scrollTo(), click())

        onView(isRoot()).perform(waitFor(1000))

        val appCompatEditText = onView(
            allOf(
                withId(R.id.layout_reset_password_email__et_email),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_reset_password_email__mc_email),
                        childAtPosition(
                            withId(R.id.layout_reset_password_email__rl_root),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("test3@test.com"), closeSoftKeyboard())

        val materialButton = onView(
            allOf(
                withId(R.id.frg_login__mb_action_button), withText("Получить код"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    1
                )
            )
        )
        materialButton.perform(scrollTo(), click())

        onView(isRoot()).perform(waitFor(1000))

        val appCompatEditText2 = onView(
            allOf(
                withId(R.id.layout_reset_password_email__et_code),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_reset_password_email__mc_code),
                        childAtPosition(
                            withId(R.id.layout_reset_password_email__rl_root),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )

        onView(isRoot()).perform(waitFor(1000))
        appCompatEditText2.perform(replaceText("TEST1"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.frg_login__mb_action_button), withText("Изменить пароль"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    1
                )
            )
        )
        onView(isRoot()).perform(waitFor(1000))

        materialButton2.perform(scrollTo(), click())

        onView(isRoot()).perform(waitFor(1000))

        val randomInt = Random(System.currentTimeMillis()).nextInt(until = 10000)

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.layout_new_password_email__et_password),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_new_password_email__mc_password),
                        childAtPosition(
                            withId(R.id.layout_new_password__rl_root),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("test${randomInt}_@#$%"), closeSoftKeyboard())

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.layout_new_password_email__et_conf),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_reset_password_email__mc_conf),
                        childAtPosition(
                            withId(R.id.layout_new_password__rl_root),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("test${randomInt}_@#$%"), closeSoftKeyboard())

        val materialButton3 = onView(
            allOf(
                withId(R.id.frg_login__mb_action_button), withText("Изменить пароль"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    1
                )
            )
        )
        materialButton3.perform(scrollTo(), click())

        onView(isRoot()).perform(waitFor(1000))

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.layout_login__et_login),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_login__mc_login),
                        childAtPosition(
                            withId(R.id.layout_login__rl_root),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText5.perform(replaceText("testlogin_3"), closeSoftKeyboard())

        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.layout_login__et_password),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_login__mc_password),
                        childAtPosition(
                            withId(R.id.layout_login__rl_root),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText6.perform(replaceText("test${randomInt}_@#$%"), closeSoftKeyboard())

        val materialButton4 = onView(
            allOf(
                withId(R.id.frg_login__mb_action_button), withText("ВОЙТИ"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    1
                )
            )
        )
        materialButton4.perform(scrollTo(), click())

        onView(isRoot()).perform(waitFor(1000))

        val textView2 = onView(
            allOf(
                withId(R.id.frg_blog__toolbar_title), withText("ГЛАВНАЯ"),
                withParent(
                    allOf(
                        withId(R.id.frg_blog__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView2.check(ViewAssertions.matches(withText("ГЛАВНАЯ")))
        mActivityTestRule.finishActivity()
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
