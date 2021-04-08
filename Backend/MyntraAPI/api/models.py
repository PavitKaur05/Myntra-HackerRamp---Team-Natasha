from django.db import models

# Create your models here.

class Product(models.Model):
    productId=models.AutoField(primary_key=True)
    productName=models.CharField(max_length=30, blank=False, null=False)
    productImageLink=models.CharField(max_length=250, blank=False, null=False)
    productBrand=models.CharField(max_length=30, blank=False, null=False)
    productPrice=models.BigIntegerField(blank=False, null=False)