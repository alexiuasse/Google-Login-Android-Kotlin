# Google-Login-Android-Kotlin
## Snippet for login with google in android.

I am using the **Django** as backend server with **django-allauth**.

In the django:

```
urlpatterns = [
  path('auth/google/', GoogleLogin.as_view(), name='google_login'),
]

from rest_auth.registration.views import SocialLoginView
from allauth.socialaccount.providers.oauth2.client import OAuth2Client
from allauth.socialaccount.providers.google.views import GoogleOAuth2Adapter
class GoogleLogin(SocialLoginView):
    adapter_class = GoogleOAuth2Adapter
    client_class = OAuth2Client
```

**Quick note 1**

I am using the webclient oauth2 and not the android one.

**Quick note 2**

Renember to get the right SHA-1 Key and set up in firebase.
There is two types of SHA-1 key, debug and release.

The **debug** you can get from android studio:

- Run your project
- Click on Gradle menu
- Expand Gradle Tasks tree
- Double click on android -> signingReport

The **release** you can get from android studio in the similiar way or if you send the .aab to you google play console (GPC), you can retrieve the SHA-1 in console, just go to your app -> click at the rocket -> signature

**If you mess up the SHA-1 key, the login account will return null**

And that's all, you just need to send the access_token retrieved from android client and everything will (with lucky and some of jedi force) work.
