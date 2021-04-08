from django.shortcuts import render
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework import viewsets
from .serializers import *
import base64
import requests
from rest_framework import status
from .serializers import *
# Create your views here.

class ProductViewSet(viewsets.ModelViewSet):
    serializer_class=ProductSerializer
    queryset=Product.objects.all()


class GetProducts(APIView):
    def get(self,request):
        products=Product.objects.all()
        res=[]
        for product in products:
            product_data=ProductSerializer(product).data
            product_data['base64']=base64.b64encode(requests.get(product.productImageLink).content)
            res.append(product_data)
        return Response(data=res, status=status.HTTP_200_OK)

