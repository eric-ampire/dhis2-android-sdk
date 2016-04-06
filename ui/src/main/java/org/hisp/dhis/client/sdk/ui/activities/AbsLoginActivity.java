/*
 * Copyright (c) 2016, University of Oslo
 *
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.client.sdk.ui.activities;

import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.hisp.dhis.client.sdk.ui.R;
import org.hisp.dhis.client.sdk.ui.views.callbacks.AbsTextWatcher;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;
import fr.castorflex.android.circularprogressbar.CircularProgressDrawable;

import static android.text.TextUtils.isEmpty;
import static org.hisp.dhis.client.sdk.ui.utils.Preconditions.isNull;

public abstract class AbsLoginActivity extends AppCompatActivity {
    private static final String IS_LOADING = "state:isLoading";


    //--------------------------------------------------------------------------------------
    // Views
    //--------------------------------------------------------------------------------------

    // ProgressBar.
    private CircularProgressBar progressBar;

    // Fields and corresponding container.
    private ViewGroup loginLayoutContent;
    private ViewGroup loginViewsContainer;
    private EditText serverUrl;
    private EditText username;
    private EditText password;
    private Button logInButton;


    //--------------------------------------------------------------------------------------
    // Animations
    //--------------------------------------------------------------------------------------

    // LayoutTransition (for JellyBean+ devices only)
    private LayoutTransition layoutTransition;

    // Animations for pre-JellyBean devices
    private Animation layoutTransitionSlideIn;
    private Animation layoutTransitionSlideOut;

    // Action which should be executed after animation is finished
    private OnPostAnimationRunnable onPostAnimationAction;

    // Callback which will be triggered when animations are finished
    private OnPostAnimationListener onPostAnimationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configuring progress bar (setting width of 6dp)
        float progressBarStrokeWidth = getResources()
                .getDimensionPixelSize(R.dimen.progressbar_stroke_width);
        progressBar = (CircularProgressBar) findViewById(R.id.progress_bar_circular);
        progressBar.setIndeterminateDrawable(new CircularProgressDrawable.Builder(this)
                .color(ContextCompat.getColor(this, R.color.color_primary_default))
                .style(CircularProgressDrawable.STYLE_ROUNDED)
                .strokeWidth(progressBarStrokeWidth)
                .rotationSpeed(1f)
                .sweepSpeed(1f)
                .build());

        loginLayoutContent = (RelativeLayout) findViewById(R.id.layout_content);
        loginViewsContainer = (CardView) findViewById(R.id.layout_login_views);
        logInButton = (Button) findViewById(R.id.button_log_in);

        serverUrl = (EditText) findViewById(R.id.edittext_server_url);
        username = (EditText) findViewById(R.id.edittext_username);
        password = (EditText) findViewById(R.id.edittext_password);

        FieldTextWatcher watcher = new FieldTextWatcher();
        serverUrl.addTextChangedListener(watcher);
        username.addTextChangedListener(watcher);
        password.addTextChangedListener(watcher);

        onPostAnimationListener = new OnPostAnimationListener();

        /* adding transition animations to root layout */
        if (isGreaterThanOrJellyBean()) {
            layoutTransition = new LayoutTransition();
            layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
            layoutTransition.addTransitionListener(onPostAnimationListener);

            loginLayoutContent.setLayoutTransition(layoutTransition);
        } else {
            layoutTransitionSlideIn = AnimationUtils.loadAnimation(this, R.anim.in_up);
            layoutTransitionSlideOut = AnimationUtils.loadAnimation(this, R.anim.out_down);

            layoutTransitionSlideIn.setAnimationListener(onPostAnimationListener);
            layoutTransitionSlideOut.setAnimationListener(onPostAnimationListener);
        }

        hideProgress();
        onTextChanged();

        logInButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onStartLoading();

                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onFinishLoading();
                    }
                }, 3000);
//                onLogInButtonClicked(serverUrl.getText(), username.getText(),
//                        password.getText());
            }
        });
    }

    @Override
    protected void onPause() {
        if (onPostAnimationAction != null) {
            onPostAnimationAction.run();
            onPostAnimationAction = null;
        }

        super.onPause();
    }

    @Override
    protected final void onSaveInstanceState(Bundle outState) {
        if (onPostAnimationAction != null) {
            outState.putBoolean(IS_LOADING,
                    onPostAnimationAction.isProgressBarWillBeShown());
        } else {
            outState.putBoolean(IS_LOADING, progressBar.isShown());
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected final void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null &&
                savedInstanceState.getBoolean(IS_LOADING, false)) {
            showProgress();
        } else {
            hideProgress();
        }

        super.onRestoreInstanceState(savedInstanceState);
    }

    public void navigateTo(final Class<? extends Activity> activityClass) {
        isNull(activityClass, "Target activity must not be null");

        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
        overridePendingTransition(
                R.anim.activity_open_enter,
                R.anim.activity_open_exit
        );
    }

    private void showProgress() {
        if (layoutTransitionSlideOut != null) {
            System.out.println("### STARTED: " + layoutTransitionSlideOut.hasStarted() +
                    " ###ENDED: " + layoutTransitionSlideOut.hasEnded());
            loginViewsContainer.startAnimation(layoutTransitionSlideOut);
            System.out.println("**** STARTED: " + layoutTransitionSlideOut.hasStarted() +
                    " **** ENDED: " + layoutTransitionSlideOut.hasEnded());
        }

        loginViewsContainer.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        if (layoutTransitionSlideIn != null) {
            loginViewsContainer.startAnimation(layoutTransitionSlideIn);
        }

        loginViewsContainer.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void onTextChanged() {
        logInButton.setEnabled(
                !isEmpty(serverUrl.getText()) &&
                        !isEmpty(username.getText()) &&
                        !isEmpty(password.getText()));
    }

    private boolean isAnimationInProgress() {
        boolean layoutTransitionAnimationsInProgress =
                layoutTransition != null && layoutTransition.isRunning();
        boolean layoutTransitionAnimationSlideUpInProgress = layoutTransitionSlideIn != null &&
                layoutTransitionSlideIn.hasStarted() && !layoutTransitionSlideIn.hasEnded();
        boolean layoutTransitionAnimationSlideOutInProgress = layoutTransitionSlideOut != null &&
                layoutTransitionSlideOut.hasStarted() && !layoutTransitionSlideOut.hasEnded();

        return layoutTransitionAnimationsInProgress || layoutTransitionAnimationSlideUpInProgress ||
                layoutTransitionAnimationSlideOutInProgress;
    }

    private static boolean isGreaterThanOrJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * Should be called in order to show progressbar.
     */
    protected final void onStartLoading() {
        if (isAnimationInProgress()) {
            onPostAnimationAction = new OnPostAnimationRunnable(null, this, true);
        } else {
            showProgress();
        }
    }

    protected final void onFinishLoading() {
        onFinishLoading(null);
    }

    /**
     * Should be called after the loading is complete.
     */
    protected final void onFinishLoading(OnAnimationFinishListener listener) {
        if (isAnimationInProgress()) {
            onPostAnimationAction = new OnPostAnimationRunnable(listener, this, false);
            return;
        }

        hideProgress();
        if (listener != null) {
            listener.onFinish();
        }
    }

    protected EditText getServerUrl() {
        return serverUrl;
    }

    protected EditText getUsername() {
        return username;
    }

    protected EditText getPassword() {
        return password;
    }

    protected Button getLoginButton() {
        return logInButton;
    }

    protected abstract void onLogInButtonClicked(
            Editable serverUrl, Editable username, Editable password);

    protected interface OnAnimationFinishListener {
        void onFinish();
    }

    private class FieldTextWatcher extends AbsTextWatcher {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            AbsLoginActivity.this.onTextChanged();
        }
    }

    private class OnPostAnimationListener implements TransitionListener, AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            // stub implementation
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
            // stub implementation
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            onPostAnimation();
        }

        @Override
        public void startTransition(
                LayoutTransition transition, ViewGroup container, View view, int type) {
            // stub implementation
        }

        @Override
        public void endTransition(
                LayoutTransition transition, ViewGroup container, View view, int type) {
            if (LayoutTransition.CHANGE_APPEARING == type ||
                    LayoutTransition.CHANGE_DISAPPEARING == type) {
                onPostAnimation();
            }
        }

        private void onPostAnimation() {
            if (onPostAnimationAction != null) {
                onPostAnimationAction.run();
                onPostAnimationAction = null;
            }
        }
    }

    /* since this runnable is intended to be executed on UI (not main) thread, we should
    be careful and not keep any implicit references to activities */
    private static class OnPostAnimationRunnable implements Runnable {
        private final OnAnimationFinishListener listener;
        private final AbsLoginActivity loginActivity;
        private final boolean showProgress;

        public OnPostAnimationRunnable(OnAnimationFinishListener listener,
                                       AbsLoginActivity loginActivity, boolean showProgress) {
            this.listener = listener;
            this.loginActivity = loginActivity;
            this.showProgress = showProgress;
        }

        @Override
        public void run() {
            if (loginActivity != null) {
                if (showProgress) {
                    loginActivity.showProgress();
                } else {
                    loginActivity.hideProgress();
                }
            }

            if (listener != null) {
                listener.onFinish();
            }
        }

        public boolean isProgressBarWillBeShown() {
            return showProgress;
        }
    }
}