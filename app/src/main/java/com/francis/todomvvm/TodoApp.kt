package com.francis.todomvvm

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// Give access to application through dependency injection
@HiltAndroidApp
class TodoApp: Application()