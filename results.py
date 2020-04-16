
def m():
        f = open( " C:\Users\sanja\StudioProjects\myfirstapp\10_21_19results.txt ", 'r')
        l = []
        for line in f:
                line.strip("I: ")
                line = line.split(",")
                l.append(line)
        f.close()
        import csv
        col_name = ["timestamp" , "acceleration value"]

        col_name = col_name + l
        csv.register_dialect('myDialect',quoting=csv.QUOTE_ALL,skipinitialspace=True)
        with open('results10_22_19.csv', 'w') as fp:
            writer = csv.writer(fp, dialect='myDialect')
            for row in col_name:
                writer.writerow(row)
        fp.close()
        '''text_file = open("Name_of_File.txt", "r")
        lines = text_file.readlines()
        #print len(lines)
        text_file.close()
        mycsv = csv.writer(open('OutPut.csv', 'wb'))
        mycsv.writerow(['timestamp', 'acceleration'])
        for line in lines:
                line.strip("I: ")
                line = line.split(",")
                l.append(line)'''
        


        

m()

	
