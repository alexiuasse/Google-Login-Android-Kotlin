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

And that's all, you just need to send the access_token retrieved from android client and everything will (with lucky and some of jedi force) work.
