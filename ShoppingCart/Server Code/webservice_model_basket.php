<?php

class webservice_model_basket extends CI_Model 
{


	public function getproducts($prodSearch)
	{

		$returnVar = Array();

		$productDetails = array();

		//$this->db->where('productName',strval($userId));
		//$query = $this->db->get('access_grid');

		$query = "SELECT distinct product_code, product_name, product_barcode, product_grammage,
						product_catcode, product_catname, product_catsubcode, product_subcodename, product_mrp,
						product_bbprice
						FROM `product_details`
						WHERE (product_name LIKE '%".$prodSearch."%'
						OR product_barcode LIKE '%".$prodSearch."%')";

		$query = $this->db->query($query);


		$productlists = $query->result();

		$i=0;

		foreach($productlists as $res){

			$productlist = array(); 
			$ar = get_object_vars($res);
			$productlist['ProductCode'] = $ar['product_code'];
			$productlist['ProductName'] = $ar['product_name'];
			$productlist['ProductGrammage'] = $ar['product_grammage'];
			$productlist['ProductBarcode'] = $ar['product_barcode'];
			$productlist['ProductCatCode'] = $ar['product_catcode'];
			$productlist['ProductCatName'] = $ar['product_catname'];
			$productlist['ProductSubCode'] = $ar['product_catsubcode'];
			$productlist['ProductSubCodeName'] = $ar['product_subcodename'];
			$productlist['ProductMRP'] = $ar['product_mrp'];
			$productlist['ProductBBPrice'] = $ar['product_bbprice'];
			
			$productDetails[$i] = $productlist;
			$i = $i+1;
		}

		$returnVar[0] = $productDetails;

		return $returnVar;
	}

	public function getproductsbycat($prodSearch)
	{

		$returnVar = Array();

		$productDetails = array();

		$query = "SELECT distinct product_code, product_name, product_barcode, product_grammage,
						product_catcode, product_catname, product_catsubcode, product_subcodename, product_mrp,
						product_bbprice
						FROM `product_details`
						WHERE (product_catsubcode LIKE '%".$prodSearch."%')";

		$query = $this->db->query($query);


		$productlists = $query->result();

		$i=0;

		foreach($productlists as $res){

			$productlist = array(); 
			$ar = get_object_vars($res);
			$productlist['ProductCode'] = $ar['product_code'];
			$productlist['ProductName'] = $ar['product_name'];
			$productlist['ProductGrammage'] = $ar['product_grammage'];
			$productlist['ProductBarcode'] = $ar['product_barcode'];
			$productlist['ProductCatCode'] = $ar['product_catcode'];
			$productlist['ProductCatName'] = $ar['product_catname'];
			$productlist['ProductSubCode'] = $ar['product_catsubcode'];
			$productlist['ProductSubCodeName'] = $ar['product_subcodename'];
			$productlist['ProductMRP'] = $ar['product_mrp'];
			$productlist['ProductBBPrice'] = $ar['product_bbprice'];
			
			$productDetails[$i] = $productlist;
			$i = $i+1;
		}

		$returnVar[0] = $productDetails;

		return $returnVar;
	}
	
}

	