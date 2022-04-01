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
class SignUpTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(RootActivity::class.java, true)

    @Test
    fun signUpTest() {
        onView(isRoot()).perform(waitFor(1000))
        val materialTextView = onView(
            allOf(
                withId(R.id.frg_login__tv_no_account), withText("Нет аккаунта? Зарегистрироваться"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    2
                )
            )
        )
        materialTextView.perform(scrollTo(), click())

        onView(isRoot()).perform(waitFor(1000))

        val button = onView(
            allOf(
                withId(R.id.frg_login__mb_action_button), withText("Создать аккаунт"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.ScrollView::class.java))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val randomNumber = Random(System.currentTimeMillis()).nextInt(until = 100000)

        val appCompatEditText = onView(
            allOf(
                withId(R.id.layout_signin__et_login),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_signin__mc_login),
                        childAtPosition(
                            withId(R.id.layout_signin__rl_root),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(replaceText("testlogin_$randomNumber"), closeSoftKeyboard())
        appCompatEditText.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.filters.all { it is InputFilter.LengthFilter && it.max == 20 })
        }

        val appCompatEditText3 = onView(
            allOf(
                withId(R.id.layout_signin__et_name),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_signin__mc_name),
                        childAtPosition(
                            withId(R.id.layout_signin__rl_root),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText3.perform(replaceText("name test"), closeSoftKeyboard())
        appCompatEditText3.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.filters.all { it is InputFilter.LengthFilter && it.max == 50 })
        }

        val appCompatEditText4 = onView(
            allOf(
                withId(R.id.layout_signin__et_email),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_signin__mc_email),
                        childAtPosition(
                            withId(R.id.layout_signin__rl_root),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText4.perform(replaceText("test$randomNumber@test.com"))
        appCompatEditText4.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.filters.all { it is InputFilter.LengthFilter && it.max == 20 })
        }

        val appCompatEditText5 = onView(
            allOf(
                withId(R.id.layout_signin__et_email), withText("test$randomNumber@test.com"),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_signin__mc_email),
                        childAtPosition(
                            withId(R.id.layout_signin__rl_root),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText5.perform(closeSoftKeyboard())

        val appCompatEditText6 = onView(
            allOf(
                withId(R.id.layout_signin__et_password),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_signin__mc_password),
                        childAtPosition(
                            withId(R.id.layout_signin__rl_root),
                            3
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText6.perform(replaceText("testpass_@#$%"), closeSoftKeyboard())
        appCompatEditText6.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.inputType - 1 == InputType.TYPE_TEXT_VARIATION_PASSWORD)
        }

        val appCompatEditText7 = onView(
            allOf(
                withId(R.id.layout_signin__et_password_conf),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_signin__mc_password_conf),
                        childAtPosition(
                            withId(R.id.layout_signin__rl_root),
                            4
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText7.perform(replaceText("testpass_@#$%"), closeSoftKeyboard())
        appCompatEditText7.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.inputType - 1 == InputType.TYPE_TEXT_VARIATION_PASSWORD)
        }

        val materialButton = onView(
            allOf(
                withId(R.id.frg_login__mb_action_button), withText("Создать аккаунт"),
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

        val appCompatEditText8 = onView(
            allOf(
                withId(R.id.layout_conf__et_code),
                childAtPosition(
                    allOf(
                        withId(R.id.layout_conf__mc_code),
                        childAtPosition(
                            withId(R.id.layout_conf__rl_root),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatEditText8.perform(replaceText("TEST1"), closeSoftKeyboard())

        val materialButton2 = onView(
            allOf(
                withId(R.id.frg_login__mb_action_button), withText("Создать аккаунт"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    1
                )
            )
        )
        materialButton2.perform(scrollTo(), click())

        onView(isRoot()).perform(waitFor(1000))

        val textView = onView(
            allOf(
                withId(R.id.frg_login__toolbar_title), withText("АВТОРИЗАЦИЯ"),
                withParent(
                    allOf(
                        withId(R.id.frg_login__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.widget.RelativeLayout::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("АВТОРИЗАЦИЯ")))

        onView(isRoot()).perform(waitFor(1000))

        val appCompatEditText11 = onView(
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
        appCompatEditText11.perform(replaceText("testlogin_$randomNumber"), closeSoftKeyboard())

        appCompatEditText11.check { view, noViewFoundException ->
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
        appCompatEditText2.perform(replaceText("testpass_@#$%"), closeSoftKeyboard())
        appCompatEditText2.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.inputType - 1 == InputType.TYPE_TEXT_VARIATION_PASSWORD)
        }

        appCompatEditText2.check { view, noViewFoundException ->
            view as AppCompatEditText
            assert(view.filters.all { it is InputFilter.LengthFilter && it.max == 20 })
        }

        val materialButton3 = onView(
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
        materialButton3.check { view, noViewFoundException ->
            view as Button
            assert(view.isEnabled)
        }
        materialButton3.perform(scrollTo(), click())

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
