PanW {
     *ar{

        |in,pos=0,offset=2,path,level=0.5|
		var beta,n_channels,max,transition_node,range,data,data_x,alpha,factor,dummy,input,signal;

		data = List[];
		data_x = List[];
		range = List[];
	    input = in;

        beta = pi/offset;
		n_channels = path.size;
		max = (path.size-1)*beta;
		transition_node = ((offset+2)/2).floor;
        alpha = n_channels-transition_node;

	    dummy = (((offset-0.5)/2).floor);
	    factor = ((offset/2)-dummy);

	if(n_channels<=transition_node,{
	n_channels.do({
				range.add([0,beta*(n_channels-1)])});},
			{
    n_channels.do({arg i;
	if(i<alpha,{data.add(offset+(2*i)/2)},{data.add(n_channels-1)});
	if(i<transition_node,{data_x.add(0)},{data_x.add(factor+(i-transition_node))});
          });
    range = [data_x*beta,data*beta].flop;
			});

		signal = Array.fill(n_channels,{arg j;
			Out.ar(path[j],(InRange.kr(pos*max,
			range[j][0],range[j] [1]))*cos((pos*max)-(beta*j)).abs*input*level)
                          });
		       ^signal;
	        }
         }









