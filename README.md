#FeatureSelectionFactory
This project implements different feature selection methods, such as (1) filter-base ranking methods; (2) Filter-based subset methods; (3) FECAR； (4) Wrapper-base methods. The details are as following:


 
##(1)Filter-based ranking methods

###	(1.1 statistic based)
		ChiSquared + Ranker

###	(1.2 probability-based)
		GainRatio + Ranker
		InfoGain + Ranker
		SymmetricalUncert + Ranker

###	(1.3 instance based)
		ReliefF + Ranker

###	(1.4 classifier based )
		SVMAttribute  + Ranker
		OneRAttribute  + Ranker

##(2)Filter-based subset methods

###	(2.1 correlation-based )
		CfsSubsetEval + GreedyStepwise

###	(2.2 consistency-based)
		ConsistencySubsetEval + GreedyStepwise

##(3)
		FECAR


##(4) Wrapper-based

###	(4.1)
	wrapper based greedy forward selection
###	(4.2)
	wrapper based greedy backward selection


