<?php defined('BASEPATH') OR exit('No direct script access allowed');

require(APPPATH.'/libraries/REST_Controller.php');

class Lawgo extends REST_Controller{


	//------ this is for the BasketBuddy Test Application

	function products_get(){
		$userName = $this->get('user');
		$prodSearch = $this->get('search');
		
		$this->load->model("webservice_model_basket");
		$status = array();

		$returnArray = $this->webservice_model_basket->getproducts($prodSearch);

		$status['ProductList'] = $returnArray[0];			

		$this->response($status,200);

	}

	//------ this is for the BasketBuddy Test Application

	function productsbycat_get(){
		$userName = $this->get('user');
		$prodSearch = $this->get('searchcat');
		
		$this->load->model("webservice_model_basket");
		$status = array();

		$returnArray = $this->webservice_model_basket->getproductsbycat($prodSearch);

		$status['ProductListByCat'] = $returnArray[0];			

		$this->response($status,200);

	}

	function city_get(){
		
		$output = array();
		$citydetails = array();

		$output['cityname'] = "Delhi";
		$citydetails[0] = $output;

		$output['cityname'] = "Gurgaon";
		$citydetails[1] = $output;

		$output['cityname'] = "Noida";
		$citydetails[2] = $output;
		
		$output1['city'] = $citydetails;
		$this->response($output1,200);
	}

	function order_get()
	{
		
		$userID = $this->get('userid');
		$address = $this->get('address');
		$itemCount = $this->get('itemcount');
		$orderList = $this->get('orderlist');
		

		$status = array();
		$status['Status'] = "Success";
		$status['OrderNo'] = "103320393";
		
		$this->response($status,200);
		
	}



}