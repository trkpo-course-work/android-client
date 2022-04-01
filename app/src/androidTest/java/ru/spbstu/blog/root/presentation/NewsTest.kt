package ru.spbstu.blog.root.presentation


import android.view.View
import android.view.ViewGroup
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
import ru.spbstu.blog.root.presentation.utils.atPosition
import ru.spbstu.blog.root.presentation.utils.waitFor

@LargeTest
@RunWith(AndroidJUnit4::class)
class NewsTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(RootActivity::class.java, true)

    @Test
    fun newsTest() {
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

        val imageButton = onView(
            allOf(
                withId(R.id.frg_blog__refresh),
                withParent(
                    allOf(
                        withId(R.id.frg_blog__toolbar),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val textView = onView(
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
        textView.check(matches(withText("ГЛАВНАЯ")))

        val viewGroup = onView(
            withId(R.id.frg_blog__rv_posts),
        )
        viewGroup.check(matches(atPosition(0, isDisplayed())))

        val viewGroup2 = onView(
            withId(R.id.frg_blog__rv_posts),
        )
        viewGroup2.check(matches(atPosition(1, isDisplayed())))

        val circleImageView = onView(
            allOf(
                withId(R.id.item_post__iv_avatar),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.frg_blog__rv_posts),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        circleImageView.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val textView2 = onView(
            allOf(
                withId(R.id.frg__user_profile__toolbar_title),
                withParent(
                    allOf(
                        withId(R.id.frg__user_profile__toolbar),
                        withParent(withId(R.id.frg_user_profile__appBarLayout))
                    )
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(isDisplayed()))

        val imageView3 = onView(
            allOf(
                withId(R.id.frg__user_profile__ib_favorite),
                withParent(
                    allOf(
                        withId(R.id.frg__user_profile__toolbar),
                        withParent(withId(R.id.frg_user_profile__appBarLayout))
                    )
                ),
                isDisplayed()
            )
        )
        imageView3.check(matches(isDisplayed()))

        val imageView4 = onView(
            allOf(
                withId(R.id.frg__user_profile__iv_back),
                withParent(
                    allOf(
                        withId(R.id.frg__user_profile__toolbar),
                        withParent(withId(R.id.frg_user_profile__appBarLayout))
                    )
                ),
                isDisplayed()
            )
        )
        imageView4.check(matches(isDisplayed()))

        val imageView5 = onView(
            allOf(
                withId(R.id.frg__user_profile__iv_back),
                withParent(
                    allOf(
                        withId(R.id.frg__user_profile__toolbar),
                        withParent(withId(R.id.frg_user_profile__appBarLayout))
                    )
                ),
                isDisplayed()
            )
        )
        imageView5.check(matches(isDisplayed()))

        val appCompatImageView = onView(
            allOf(
                withId(R.id.frg__user_profile__iv_back),
                childAtPosition(
                    allOf(
                        withId(R.id.frg__user_profile__toolbar),
                        childAtPosition(
                            withId(R.id.frg_user_profile__appBarLayout),
                            1
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        appCompatImageView.perform(click())
        onView(isRoot()).perform(waitFor(1000))

        val appCompatImageButton = onView(
            allOf(
                withId(R.id.frg_blog__refresh),
                childAtPosition(
                    allOf(
                        withId(R.id.frg_blog__toolbar),
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

        val viewGroup4 = onView(
            withId(R.id.frg_blog__rv_posts),
        )
        viewGroup4.check(matches(atPosition(0, isDisplayed())))

        val viewGroup5 = onView(
            withId(R.id.frg_blog__rv_posts),
        )
        viewGroup5.check(matches(atPosition(1, isDisplayed())))
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
