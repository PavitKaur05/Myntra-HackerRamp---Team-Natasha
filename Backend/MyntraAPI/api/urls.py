from django.urls import path
from django.conf.urls import url
from .views import *
from django.conf.urls import include #for viewset
from rest_framework.routers import DefaultRouter 

router=DefaultRouter()
# router.register('product',ProductViewSet)
urlpatterns=[
    path('product/',GetProducts.as_view(),name='products')
# url(r'',include(router.urls))
]