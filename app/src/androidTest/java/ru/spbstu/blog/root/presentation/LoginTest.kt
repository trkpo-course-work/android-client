package ru.spbstu.blog.root.presentation


import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatEditText
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import ru.spbstu.blog.R
import ru.spbstu.blog.root.presentation.utils.waitFor


@LargeTest
@RunWith(AndroidJUnit4::class)
class LoginTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(RootActivity::class.java)

    @Test
    fun loginTest() {
        onView(isRoot()).perform(waitFor(1000))
        // 1st try with incorrect symbols in login
        val appCompatEditText4 = onView(
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
        appCompatEditText4.perform(replaceText("viruskulsz%$@()"), closeSoftKeyboard())

        appCompatEditText4.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.filters.all { it is InputFilter.LengthFilter && it.max == 20 })
        }

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
        materialButton4.check { view, noViewFoundException ->
            view as Button
            assert(!view.isEnabled)
        }

        val appCompatEditText5 = onView(
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
        appCompatEditText5.perform(replaceText("imo39067"), closeSoftKeyboard())

        appCompatEditText5.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.filters.all { it is InputFilter.LengthFilter && it.max == 20 })
        }

        val materialButton6 = onView(
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
        materialButton6.check { view, noViewFoundException ->
            view as Button
            assert(view.isEnabled)
        }
        materialButton6.perform(scrollTo(), click())

        onView(isRoot()).perform(waitFor(1000))

        // 2nd try with incorrect symbols in pass
        val appCompatEditText7 = onView(
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
        appCompatEditText7.perform(replaceText("viruskuls"), closeSoftKeyboard())

        appCompatEditText7.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.filters.all { it is InputFilter.LengthFilter && it.max == 20 })
        }

        val materialButton8 = onView(
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
        materialButton8.check { view, noViewFoundException ->
            view as Button
            assert(view.isEnabled)
        }

        val appCompatEditText9 = onView(
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
        appCompatEditText9.perform(replaceText("imo39067$%*)(()"), closeSoftKeyboard())

        appCompatEditText9.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.filters.all { it is InputFilter.LengthFilter && it.max == 20 })
        }

        val materialButton0 = onView(
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
        materialButton0.check { view, noViewFoundException ->
            view as Button
            assert(view.isEnabled)
        }
        materialButton0.perform(scrollTo(), click())

        onView(isRoot()).perform(waitFor(1000))

        // 3rd try correct
        val appCompatEditText = onView(
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
        appCompatEditText.perform(replaceText("testlogin1_"), closeSoftKeyboard())

        appCompatEditText.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.filters.all { it is InputFilter.LengthFilter && it.max == 20 })
        }

        val materialButton1 = onView(
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
        materialButton1.check { view, noViewFoundException ->
            view as Button
            assert(view.isEnabled)
        }

        val appCompatEditText2 = onView(
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
        appCompatEditText2.perform(replaceText("test4629_@#$%"), closeSoftKeyboard())
        appCompatEditText2.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.inputType - 1 == InputType.TYPE_TEXT_VARIATION_PASSWORD)
        }

        appCompatEditText2.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.filters.all { it is InputFilter.LengthFilter && it.max == 20 })
        }

        val materialButton = onView(
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
        materialButton.check { view, noViewFoundException ->
            view as Button
            assert(view.isEnabled)
        }
        materialButton.perform(scrollTo(), click())

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
        textView2.check(matches(withText("ГЛАВНАЯ")))

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
