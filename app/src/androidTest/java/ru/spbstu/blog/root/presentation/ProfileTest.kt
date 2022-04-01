package ru.spbstu.blog.root.presentation


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
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

@LargeTest
@RunWith(AndroidJUnit4::class)
class ProfileTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(RootActivity::class.java)

    @Test
    fun profileTest() {
        onView(isRoot()).perform(waitFor(1000))
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
        appCompatEditText.perform(replaceText("viruskulsz"), closeSoftKeyboard())

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
        appCompatEditText2.perform(replaceText("imo39067"), closeSoftKeyboard())

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
        materialButton.perform(scrollTo(), click())
        onView(isRoot()).perform(waitFor(1000))

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.profileFragment), withContentDescription("Home"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavigationView),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val textView = onView(
            allOf(
                withId(R.id.frg_profile__toolbar_title), withText("ПРОФИЛЬ"),
                withParent(
                    allOf(
                        withId(R.id.frg_profile__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("ПРОФИЛЬ")))

        val imageButton = onView(
            allOf(
                withId(R.id.frg_profile__ib_actions),
                withParent(
                    allOf(
                        withId(R.id.frg_profile__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val textView2 = onView(
            allOf(
                withId(R.id.frg_profile__tv_favorites), withText("Перейти в избранное"),
                withParent(withParent(withId(R.id.bottomNavHost))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Перейти в избранное")))

        val textView3 = onView(
            allOf(
                withId(R.id.frg_profile__tv_login_label), withText("Логин"),
                withParent(withParent(withId(R.id.bottomNavHost))),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Логин")))

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.frg_profile__ib_actions),
                childAtPosition(
                    allOf(
                        withId(R.id.frg_profile__toolbar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val textView5 = onView(
            allOf(
                withId(android.R.id.title), withText("Редактировать"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("Редактировать")))

        val textView6 = onView(
            allOf(
                withId(android.R.id.title), withText("Выйти"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView6.check(matches(withText("Выйти")))

        val textView7 = onView(
            allOf(
                withId(android.R.id.title), withText("Удалить"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView7.check(matches(withText("Удалить")))

        val materialTextView = onView(
            allOf(
                withId(android.R.id.title), withText("Редактировать"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.android.internal.view.menu.ListMenuItemView")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val textView9 = onView(
            allOf(
                withId(R.id.frg_edit_profile__toolbar_title), withText("РЕДАКТИРОВАНИЕ"),
                withParent(
                    allOf(
                        withId(R.id.frg_edit_profile__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView9.check(matches(withText("РЕДАКТИРОВАНИЕ")))

        val imageButton2 = onView(
            allOf(
                withId(R.id.frg_edit_profile__ib_actions),
                withParent(
                    allOf(
                        withId(R.id.frg_edit_profile__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        imageButton2.check(matches(isDisplayed()))

        val appCompatImageButton2 = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.frg_edit_profile__toolbar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        appCompatImageButton2.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val appCompatTextView = onView(
            allOf(
                withId(R.id.frg_profile__tv_favorites), withText("Перейти в избранное"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottomNavHost),
                        0
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        appCompatTextView.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val textView10 = onView(
            allOf(
                withId(R.id.frg_favorites__toolbar_title), withText("ИЗБРАННОЕ"),
                withParent(
                    allOf(
                        withId(R.id.frg_favorites__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView10.check(matches(withText("ИЗБРАННОЕ")))

        onView(isRoot()).perform(ViewActions.pressBack())
        onView(isRoot()).perform(waitFor(1000))

        val appCompatImageButton3 = onView(
            allOf(
                withId(R.id.frg_profile__ib_actions),
                childAtPosition(
                    allOf(
                        withId(R.id.frg_profile__toolbar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton3.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val materialTextView2 = onView(
            allOf(
                withId(android.R.id.title), withText("Удалить"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.android.internal.view.menu.ListMenuItemView")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView2.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val textView12 = onView(
            allOf(
                withId(androidx.appcompat.R.id.alertTitle), withText("Удаление профиля"),
                withParent(
                    allOf(
                        withId(androidx.appcompat.R.id.title_template),
                        withParent(withId(androidx.appcompat.R.id.topPanel))
                    )
                ),
                isDisplayed()
            )
        )
        textView12.check(matches(withText("Удаление профиля")))

        val materialButton2 = onView(
            allOf(
                withId(android.R.id.button2), withText("НЕТ"),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.buttonPanel),
                        0
                    ),
                    2
                )
            )
        )
        materialButton2.perform(scrollTo(), click())
        onView(isRoot()).perform(waitFor(1000))

        val appCompatImageButton4 = onView(
            allOf(
                withId(R.id.frg_profile__ib_actions),
                childAtPosition(
                    allOf(
                        withId(R.id.frg_profile__toolbar),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatImageButton4.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val materialTextView3 = onView(
            allOf(
                withId(android.R.id.title), withText("Выйти"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.android.internal.view.menu.ListMenuItemView")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView3.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val textView14 = onView(
            allOf(
                withId(androidx.appcompat.R.id.alertTitle), withText("Выход"),
                withParent(
                    allOf(
                        withId(androidx.appcompat.R.id.title_template),
                        withParent(withId(androidx.appcompat.R.id.topPanel))
                    )
                ),
                isDisplayed()
            )
        )
        textView14.check(matches(withText("Выход")))

        val materialButton3 = onView(
            allOf(
                withId(android.R.id.button1), withText("ДА"),
                childAtPosition(
                    childAtPosition(
                        withId(androidx.appcompat.R.id.buttonPanel),
                        0
                    ),
                    3
                )
            )
        )
        materialButton3.perform(scrollTo(), click())
        onView(isRoot()).perform(waitFor(1000))

        val textView16 = onView(
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
        textView16.check(matches(withText("АВТОРИЗАЦИЯ")))
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
