'''
Automation Testing dojo results listener. Sends results of test execution to AT dojo server
server address and user name should be defined in suite metadata. 

@author: serhiy.zelenin
'''
import httplib
import urllib
import re

class DojoListener:

    ROBOT_LISTENER_API_VERSION = 2
    testResults = []

    def start_suite(self, name, attrs):
        metadata = attrs['metadata']
        self.userName = metadata['userName']
        metadataServer = metadata['server']
        m = re.search('http://(.*):(\d*)', metadataServer)
        self.address = m.group(1)
        self.port = int(m.group(2))
        print 'Will report to: ' + metadataServer

    def end_test(self, name, attrs):
        tags = attrs['tags']
        for tag in tags:
            scenarioNumber = tag.lower().replace('scenario', '').strip()
            if scenarioNumber.isdigit() :
                self.testResults.append(('scenario'+scenarioNumber, attrs['status']))
                
                
    def end_suite(self, name, attrs):
        try :
            self.testResults.append(('name',self.userName))
            params = urllib.urlencode(self.testResults)
            headers = {"Content-type": "application/x-www-form-urlencoded","Accept": "text/plain"}
            conn = httplib.HTTPConnection(self.address, port=self.port, timeout=10)
            conn.request('POST', '/result', params, headers)
            response = conn.getresponse()
            data =  response.read()
#            print data
        except Exception as e:
            print 'Unable to report results to server. Server: ' + self.server
            raise
        finally:
            if not conn is None :
                conn.close()
            